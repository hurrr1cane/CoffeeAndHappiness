'use client';

import { createContext, useContext, useState, useEffect } from "react";

const GlobalContext = createContext({
    user: {},
    setUser: () => {},
});

export const GlobalContextProvider = ({ children }) => {
    const [user, setUser] = useState({});

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem("user", JSON.stringify(user));
    }, [user]);

    return (
        <GlobalContext.Provider value={{ user, setUser }}>
            {children}
        </GlobalContext.Provider>
    );
};

export const useGlobalContext = () => useContext(GlobalContext);
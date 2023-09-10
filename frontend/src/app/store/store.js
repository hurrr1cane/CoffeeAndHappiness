'use client';

import { createContext, useContext, useState, useEffect } from "react";

const GlobalContext = createContext({
    user: {},
    setUser: () => {},
    isDark: false, // Default value for isDark
    setIsDark: () => {}, // Default value for setIsDark
});

export const GlobalContextProvider = ({ children }) => {
    const [user, setUser] = useState({});
    const [isDark, setIsDark] = useState(false); // Default value for isDark

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
        
        const storedIsDark = localStorage.getItem("isDark");
        if (storedIsDark) {
            setIsDark(JSON.parse(storedIsDark));
        }
    }, []);

    useEffect(() => {
        localStorage.setItem("user", JSON.stringify(user));
    }, [user]);

    useEffect(() => {
        localStorage.setItem("isDark", JSON.stringify(isDark));
    }, [isDark]);

    return (
        <GlobalContext.Provider value={{ user, setUser, isDark, setIsDark }}>
            {children}
        </GlobalContext.Provider>
    );
};

export const useGlobalContext = () => useContext(GlobalContext);
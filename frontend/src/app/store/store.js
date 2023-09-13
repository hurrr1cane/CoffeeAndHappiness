'use client';

import { createContext, useContext, useState, useEffect } from "react";

const GlobalContext = createContext({
    user: {},
    setUser: () => {},
    isDark: false, // Default value for isDark
    setIsDark: () => {}, // Default value for setIsDark
    language: 'en', // Default value for language
    setLanguage: () => {}, // Default value for setLanguage
});

export const GlobalContextProvider = ({ children }) => {
    const [user, setUser] = useState({});
    const [isDark, setIsDark] = useState(false); // Default value for isDark
    const [language, setLanguage] = useState('en'); // Default value for language

    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
        
        const storedIsDark = localStorage.getItem("isDark");
        if (storedIsDark) {
            setIsDark(JSON.parse(storedIsDark));
        }

        const storedLanguage = localStorage.getItem("language");
        if (storedLanguage) {
            setLanguage(storedLanguage); // No need to parse, as it's a string
        }
    }, []);

    useEffect(() => {
        localStorage.setItem("user", JSON.stringify(user));
    }, [user]);

    useEffect(() => {
        localStorage.setItem("isDark", JSON.stringify(isDark));
    }, [isDark]);

    useEffect(() => {
        localStorage.setItem("language", language); // No need to stringify, as it's a string
    }, [language]);

    return (
        <GlobalContext.Provider value={{ user, setUser, isDark, setIsDark, language, setLanguage }}>
            {children}
        </GlobalContext.Provider>
    );
};

export const useGlobalContext = () => useContext(GlobalContext);

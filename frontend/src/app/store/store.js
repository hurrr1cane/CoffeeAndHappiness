"use client"

import { createContext, useContext, useState, useEffect } from "react";

const GlobalContext = createContext({
    user: {},
    setUser: () => {},
    language: "en", // Default language
    setLanguage: () => {},
});

export const GlobalContextProvider = ({ children }) => {
    const [user, setUser] = useState({});
    const [language, setLanguage] = useState("en"); // Default language

    // Load user data from localStorage
    useEffect(() => {
        const storedUser = localStorage.getItem("user");
        if (storedUser) {
            setUser(JSON.parse(storedUser));
        }
    }, []);

    // Save user data to localStorage whenever it changes
    useEffect(() => {
        localStorage.setItem("user", JSON.stringify(user));
    }, [user]);

    // Load language preference from localStorage
    useEffect(() => {
        const storedLanguage = localStorage.getItem("language");
        if (storedLanguage) {
            setLanguage(storedLanguage);
        }
    }, []);

    // Save language preference to localStorage whenever it changes
    useEffect(() => {
        localStorage.setItem("language", language);
    }, [language]);

    return (
        <GlobalContext.Provider value={{ user, setUser, language, setLanguage }}>
            {children}
        </GlobalContext.Provider>
    );
};

export const useGlobalContext = () => useContext(GlobalContext);
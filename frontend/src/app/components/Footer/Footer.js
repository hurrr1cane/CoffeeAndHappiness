"use client"

import Icons from "./Icons"
import styles from './Footer.module.scss'
import { useGlobalContext } from "@/app/store/store"
function getYear() {
    return new Date().getFullYear() 
}

export default function Footer() {
    const { isDark, language } = useGlobalContext()

    return (
            <footer className={`${styles.footer} ${ isDark ? styles.dark : ""}`}>
                <p>{language === 'en' ? "With Love" : "З любов'ю"}</p>
                <p>{language === 'en' ? "Coffee and Happiness" : "Кава та щастя"} © {getYear()}</p>
                <div className={styles.icons}>
                <Icons/>
                </div>
            </footer>
    )
}
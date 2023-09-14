"use client"

import styles from "./MenuBar.module.scss";
import { useGlobalContext } from "@/app/store/store";
export default function MenuBar({tab, setTab}) {

  const {isDark, language} = useGlobalContext()

  return (
      <nav className={`${styles.container} ${isDark ? styles.dark : ""}`}>
        <div onClick={() => {setTab("MAIN")}} className={`${styles.menuItem} ${tab === "MAIN" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Main' : "Основні"}</p>
        </div>
        <div onClick={() => {setTab("DRINK")}} className={`${styles.menuItem} ${tab === "DRINK" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Drinks' : "Напої"}</p>
        </div>
        <div onClick={() => {setTab("COFFEE")}} className={`${styles.menuItem} ${tab === "COFFEE" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Coffee' : "Кава"}</p>
        </div>
        <div onClick={() => {setTab("SALAD")}} className={`${styles.menuItem} ${tab === "SALAD" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Salads' : "Салати"}</p>
        </div>
        <div onClick={() => {setTab("DESSERT")}} className={`${styles.menuItem} ${tab === "DESERT" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Desserts' : "Десерти"}</p>
        </div>
        <div onClick={() => {setTab("ICE_CREAM")}} className={`${styles.menuItem} ${tab === "ICE_CREAM" ? styles.selected : ''}`}>
          <p>{language === 'en' ? 'Ice cream' : "Морозиво"}</p>
        </div>
      </nav>
    );
  }

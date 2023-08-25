"use client";

import { useState } from "react";
import styles from "./MenuBar.module.scss";
export default function MenuBar() {
  const [tab, setTab] = useState();

  const tabs = [
    'main',
    'drinks',
    'coffee',
    'salads',
    'desserts',
    'icecream'
  ]

  return (
    <nav className={styles.container}>
      <div onClick={() => {setTab("main")}} className={`${styles.menuItem} ${tab === "main" ? styles.selected : ''}`}>
        <p>Main</p>
      </div>
      <div onClick={() => {setTab("drinks")}} className={`${styles.menuItem} ${tab === "drinks" ? styles.selected : ''}`}>
        <p>Drinks</p>
      </div>
      <div onClick={() => {setTab("coffee")}} className={`${styles.menuItem} ${tab === "coffee" ? styles.selected : ''}`}>
        <p>Coffee</p>
      </div>
      <div onClick={() => {setTab("salads")}} className={`${styles.menuItem} ${tab === "salads" ? styles.selected : ''}`}>
        <p>Salads</p>
      </div>
      <div onClick={() => {setTab("desserts")}} className={`${styles.menuItem} ${tab === "desserts" ? styles.selected : ''}`}>
        <p>Desserts</p>
      </div>
      <div onClick={() => {setTab("icecream")}} className={`${styles.menuItem} ${tab === "icecream" ? styles.selected : ''}`}>
        <p>Ice cream</p>
      </div>
    </nav>
  );
}

"use client";

import { useState } from "react";
import styles from "./MenuBar.module.scss";
export default function MenuBar() {
  return (
    <nav className={styles.container}>
      <div className={styles.menuItem}>
        <p>Main</p>
      </div>
      <div className={styles.menuItem}>
        <p>Drinks</p>
      </div>
      <div className={styles.menuItem}>
        <p>Desserts</p>
      </div>
      <div className={styles.menuItem}>
        <p>Coffee</p>
      </div>
      <div className={styles.menuItem}>
        <p>Ice cream</p>
      </div>
    </nav>
  );
}

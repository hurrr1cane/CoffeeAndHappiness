"use client";

import { useState } from "react";
import styles from "./MenuBar.module.scss";
export default function MenuBar() {
  return (
    <nav className={styles.container}>
      <div className={styles.menuItem}>
        <i>Main</i>
      </div>
      <div className={styles.menuItem}>
        <i>Drinks</i>
      </div>
      <div className={styles.menuItem}>
        <i>Desserts</i>
      </div>
      <div className={styles.menuItem}>
        <i>Coffee</i>
      </div>
      <div className={styles.menuItem}>
        <i>Ice cream</i>
      </div>
    </nav>
  );
}

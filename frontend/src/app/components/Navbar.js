"use client";

import Link from "next/link";
import styles from "./Navbar.module.scss";

import { useState } from "react";

export default function Navbar() {
  const [selectedTab, setSelectedTab] = useState("home");
  // Todo: add an animation for changing the page
  const handleTabClick = (tab) => {
    setSelectedTab(tab);
  };
  return (
    <nav className={styles.navbar}>

      <Link onClick={() => {handleTabClick('home')}} key={'home'} className={`${styles.link} ${selectedTab === 'home' ? styles.selected : '' }`} href="/"><h1>Home</h1></Link>
            <Link onClick={() => {handleTabClick('institutions')}} key={'institutions'} className={`${styles.link} ${selectedTab === 'institutions' ? styles.selected : '' }`} href="/institutions"><h1>Institutions</h1></Link>
            <Link onClick={() => {handleTabClick('menu')}} key={'menu'} className={`${styles.link} ${selectedTab === 'menu' ? styles.selected : '' }`} href="/menu"><h1>Menu</h1></Link>
            <Link onClick={() => {handleTabClick('about-us')}} key={'about-us'} className={`${styles.link} ${selectedTab === 'about-us' ? styles.selected : '' }`} href="/about-us"><h1>About us</h1></Link>
            <Link onClick={() => {handleTabClick('user')}} key={'user'} className={`${styles.link} ${selectedTab === 'user' ? styles.selected : '' }`} href="/user"><h1>User</h1></Link>
    </nav>
  );
}

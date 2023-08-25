  "use client";

  import Link from "next/link";
  import styles from "./Navbar.module.scss";
  import Brightness2Icon from '@mui/icons-material/Brightness2';
  import Brightness5Icon from '@mui/icons-material/Brightness5';
  import EnglishImage from '../../../public/english.png'
  import Image from "next/image";
  import { useState, useEffect, useRef } from "react";
  import { usePathname } from 'next/navigation'
  export default function Navbar() {
    const pathname = usePathname();
    const [stylesData, setStylesData] = useState({
      selectedTab: "",
      highlightLeft: 0,
      highlightWidth: 0,
      highlightHeight: 0
    });
  
    const tabRefs = {
      'home': useRef(),
      'institutions': useRef(),
      'menu': useRef(),
      'about-us': useRef(),
      'user': useRef()
    };
  
    useEffect(() => {
      const storedTab = localStorage.getItem("selectedTab") || "home";
      const routeName = pathname.replace("/", "") || "home";
      
      setStylesData(prevStyles => ({
        ...prevStyles,
        selectedTab: storedTab,
        highlightLeft: tabRefs[routeName]?.current?.offsetLeft || 0,
        highlightWidth: tabRefs[routeName]?.current?.offsetWidth || 0,
        highlightHeight: tabRefs[routeName]?.current?.offsetHeight || 0
      }));
    }, [pathname]);
  
    const handleTabClick = (tab) => {
      localStorage.setItem("selectedTab", tab);
      setStylesData(prevStyles => ({
        ...prevStyles,
        selectedTab: tab,
        highlightLeft: tabRefs[tab]?.current?.offsetLeft || 0,
        highlightWidth: tabRefs[tab]?.current?.offsetWidth || 0,
        highlightHeight: tabRefs[tab]?.current?.offsetHeight || 0
      }));  
    };
    return (
      <nav className={styles.navbar}>
        <div className={styles.highlight} style={{
        left: stylesData.highlightLeft,
        width: stylesData.highlightWidth,
        height: stylesData.highlightHeight
      }}></div>
        <Link ref={tabRefs.home} onClick={() => {handleTabClick('home')}} className={`${styles.link} ${stylesData.selectedTab === 'home' ? styles.selected : '' }`} href="/"><h1>Home</h1></Link>
        <Link ref={tabRefs.institutions} onClick={() => {handleTabClick('institutions')}} className={`${styles.link} ${stylesData.selectedTab === 'institutions' ? styles.selected : '' }`} href="/institutions"><h1>Institutions</h1></Link>
        <Link ref={tabRefs.menu} onClick={() => {handleTabClick('menu')}} className={`${styles.link} ${stylesData.selectedTab === 'menu' ? styles.selected : '' }`} href="/menu"><h1>Menu</h1></Link>
        <Link ref={tabRefs["about-us"]} onClick={() => {handleTabClick('about-us')}} className={`${styles.link} ${stylesData.selectedTab === 'about-us' ? styles.selected : '' }`} href="/about-us"><h1>About us</h1></Link>
        <Link ref={tabRefs.user} onClick={() => {handleTabClick('user')}} className={`${styles.link} ${stylesData.selectedTab === 'user' ? styles.selected : '' }`} href="/user"><h1>User</h1></Link>
        <div className={styles['icons-container']}>
          <Brightness2Icon/>
          <Image className={styles.image} src={EnglishImage} alt="image of the flag of United Kingdom"/>
        </div>
      </nav>
    );
  }

  "use client";

  import Link from "next/link";
  import styles from "./Navbar.module.scss";
  import Brightness2Icon from '@mui/icons-material/Brightness2';
  import Brightness5Icon from '@mui/icons-material/Brightness5';
  import EnglishImage from '../../../../public/english.png'
  import Image from "next/image";
  import { useState, useEffect, useRef } from "react";
  import { usePathname } from 'next/navigation'

  //Todo: fix video page lag

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
      'user': useRef(),
      'login': useRef(),
      'register':useRef()
    };
  

    useEffect(() => {
      const storedTab = localStorage.getItem("selectedTab") || "home";
      let routeName = pathname.replace("/", "") || "home";
      routeName = routeName.split('/')[0]       

      setStylesData(({
        selectedTab: (routeName === storedTab ? storedTab : routeName),
        highlightLeft: tabRefs[routeName]?.current?.offsetLeft,
        highlightWidth: tabRefs[routeName]?.current?.offsetWidth,
        highlightHeight: tabRefs[routeName]?.current?.offsetHeight
      }));
    }, [pathname]);
  
    const handleTabClick = (tab) => {
      localStorage.setItem("selectedTab", tab);
      setStylesData(({
        selectedTab: tab,
        highlightLeft: tabRefs[tab]?.current?.offsetLeft,
        highlightWidth: tabRefs[tab]?.current?.offsetWidth,
        highlightHeight: tabRefs[tab]?.current?.offsetHeight
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
        {/* <Link ref={tabRefs.user} onClick={() => {handleTabClick('user')}} className={`${styles.link} ${stylesData.selectedTab === 'user' ? styles.selected : '' }`} href="/user"><h1>User</h1></Link> */}
        <Link ref={tabRefs.login} onClick={() => {handleTabClick('login')}} className={`${styles.link} ${stylesData.selectedTab === 'login' ? styles.selected : '' }`} href="/login"><h1>Log in</h1></Link>
        <Link ref={tabRefs.register} onClick={() => {handleTabClick('register')}} className={`${styles.link} ${stylesData.selectedTab === 'register' ? styles.selected : '' }`} href="/register"><h1>Register</h1></Link>
        
        <div className={styles['icons-container']}>
          <Brightness2Icon/>
          <Image className={styles.image} src={EnglishImage} alt="image of the flag of United Kingdom"/>
        </div>
      </nav>
    );
  }

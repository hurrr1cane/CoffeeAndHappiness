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
    const isWindow = typeof window === undefined
    const [selectedTab, setSelectedTab] = useState(isWindow ? localStorage.getItem("selectedTab") ?? "home" : "home");
    useEffect(() => {
      const storedTab = localStorage.getItem("selectedTab");
      if (storedTab) {
        setSelectedTab(storedTab);
      }
    }, []);

    const tabRefs = {
      'home': useRef(),
      'institutions': useRef(),
      'menu': useRef(),
      'about-us': useRef(),
      'user': useRef()
    }
  useEffect(() => {
    const routeName = pathname.replace("/", "");
    setSelectedTab(routeName || "home");
  }, [pathname]);

  const handleTabClick = (tab) => {
    setSelectedTab(tab);
    localStorage.setItem("selectedTab", tab); 
    
  };
    return (
      <nav className={styles.navbar}>
        {/* <div className={styles.highlight} style={{left:tabRefs[selectedTab].current?.offsetLeft, width: tabRefs[selectedTab].current?.offsetWidth, height:tabRefs[selectedTab].current?.offsetHeight}}></div> */}
        <Link ref={tabRefs.home} onClick={() => {handleTabClick('home')}} className={`${styles.link} ${selectedTab === 'home' ? styles.selected : '' }`} href="/"><h1>Home</h1></Link>
        <Link ref={tabRefs.institutions} onClick={() => {handleTabClick('institutions')}} className={`${styles.link} ${selectedTab === 'institutions' ? styles.selected : '' }`} href="/institutions"><h1>Institutions</h1></Link>
        <Link ref={tabRefs.menu} onClick={() => {handleTabClick('menu')}} className={`${styles.link} ${selectedTab === 'menu' ? styles.selected : '' }`} href="/menu"><h1>Menu</h1></Link>
        <Link ref={tabRefs["about-us"]} onClick={() => {handleTabClick('about-us')}} className={`${styles.link} ${selectedTab === 'about-us' ? styles.selected : '' }`} href="/about-us"><h1>About us</h1></Link>
        <Link ref={tabRefs.user} onClick={() => {handleTabClick('user')}} className={`${styles.link} ${selectedTab === 'user' ? styles.selected : '' }`} href="/user"><h1>User</h1></Link>
        <div className={styles['icons-container']}>
          <Brightness2Icon/>
          <Image className={styles.image} src={EnglishImage} alt="image of the flag of United Kingdom"/>
        </div>
      </nav>
    );
  }

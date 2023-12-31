  "use client";

  import Link from "next/link";
  import styles from "./Navbar.module.scss";
  import Brightness2Icon from '@mui/icons-material/Brightness2';
  import Brightness5Icon from '@mui/icons-material/Brightness5';
  import { useState, useEffect, useRef } from "react";
  import { usePathname } from 'next/navigation'
  import { useGlobalContext } from "@/app/store/store";
  import { useRouter } from "next/navigation";
  import PositionedMenu from "./Menu";
  import { Fab } from "@mui/material";
  import MenuIcon from '@mui/icons-material/Menu';
  //Todo: fix element positioning
  import useWindowSize from "@/app/menu/dish/[id]/Reviews/useWindow";
  export default function Navbar() {
    const { push } = useRouter()
    const {user, setUser, isDark, setIsDark, language} = useGlobalContext()
    const [menuOpen, setMenuOpen] = useState(false)

    const handleLogout = () => {
      push('/login')
      setUser({})
    }

    const {width, height} = useWindowSize()

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
      'register': useRef(),
    };
  
    const toggleMobileMenu = () => {
      setMenuOpen((prevMenuOpen) => !prevMenuOpen)
    }

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
      <nav className={`${styles.navbar} ${menuOpen ? styles.open : ''} ${isDark ? styles.dark : ""}`}>
    
        <MenuIcon onClick={toggleMobileMenu} sx={{color: isDark ? "#CCCCCC" : "", display: width > 768 ? "none" : "block"}} className={styles.menuButton}/>
        <Link ref={tabRefs.home} onClick={() => {handleTabClick('home')}} className={`${styles.link} ${stylesData.selectedTab === 'home' ? styles.selected : '' }`} href="/"><h1>{language === 'en' ? "Home" : "Домашня"}</h1></Link>
        <Link ref={tabRefs.institutions} onClick={() => {handleTabClick('institutions')}} className={`${styles.link} ${stylesData.selectedTab === 'institutions' ? styles.selected : '' }`} href="/institutions"><h1>{language === 'en' ? "Institutions" : "Заклади"}</h1></Link>
        <Link ref={tabRefs.menu} onClick={() => {handleTabClick('menu')}} className={`${styles.link} ${stylesData.selectedTab === 'menu' ? styles.selected : '' }`} href="/menu"><h1>{language === 'en' ? "Menu" : "Меню"}</h1></Link>
        <Link ref={tabRefs["about-us"]} onClick={() => {handleTabClick('about-us')}} className={`${styles.link} ${stylesData.selectedTab === 'about-us' ? styles.selected : '' }`} href="/about-us"><h1>{language === 'en' ? "About-us" : "Про нас"}</h1></Link>
        
        {user?.firstName ? 
        <>
        <Link ref={tabRefs.user} onClick={() => {handleTabClick('user')}} className={`${styles.link} ${stylesData.selectedTab === 'user' ? styles.selected : '' }`} href="/user"><h1>{user.firstName}</h1></Link>
        <span onClick={() => {handleLogout()}} className={`${styles.link} ${styles.logout}`}><h1>{language === 'en' ? "Log out" : "Вийти"}</h1></span>  
        </>:
        <>
          <Link ref={tabRefs.login} onClick={() => {handleTabClick('login')}} className={`${styles.link} ${stylesData.selectedTab === 'login' ? styles.selected : '' }`} href="/login"><h1>{language === 'en' ? "Log in" : "Логін"}</h1></Link>
          <Link ref={tabRefs.register} onClick={() => {handleTabClick('register')}} className={`${styles.link} ${stylesData.selectedTab === 'register' ? styles.selected : '' }`} href="/register"><h1>{language === 'en' ? "Register" : "Реєстрація"}</h1></Link>
        </>}
        
        <div className={styles['icons-container']}>
        <Fab onClick={() => setIsDark(prev => !prev)} sx={{bgcolor:"inherit", "&:hover":{bgcolor:"inherit"}, boxShadow:"none"}}>
            {!isDark ? <Brightness2Icon sx={{margin: "0.25rem"}}/> : <Brightness5Icon sx={{margin: "0.25rem", color:"#CCCCCC"}}/>}
        </Fab>
          <PositionedMenu isDark={isDark}/>
        </div>
      </nav>
    );
  }

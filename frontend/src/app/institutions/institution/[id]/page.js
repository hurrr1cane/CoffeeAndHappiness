"use client"


import styles from './page.module.scss'
import Institution from './Institution'
import Reviews from './Reviews/Reviews'
import { usePathname } from 'next/navigation'

export default function Home() {

    const { pathname } = usePathname()

    return (
    <div className={styles.main}>
        {pathname}
        <Institution/>
        <h1 className={styles['reviews-header']}>Reviews:</h1>
        {/* <Reviews/> */}
     </div> 
    )
}
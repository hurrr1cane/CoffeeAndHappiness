"use client"

import styles from './page.module.scss'
import Dish from './Dish'
import Reviews from './Reviews/Reviews'
import { useState } from 'react'
import { useGlobalContext } from '@/app/store/store'
export default function Home() {

    const {isDark} = useGlobalContext()

    const [seed, setSeed] = useState(1);
       const reset = () => {
            setSeed(Math.random());
        }

    return (
    <div className={`${styles.main} ${isDark ? styles.dark : ""}`}>
        <Dish reset={reset}/>
        <h1 className={styles['reviews-header']}>Reviews:</h1>
        <Reviews key={seed}/>
     </div> 
    )
}
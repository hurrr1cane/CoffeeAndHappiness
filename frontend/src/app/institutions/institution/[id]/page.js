"use client"


import styles from './page.module.scss'
import Institution from './Institution'
import Reviews from './Reviews/Reviews'
import { useState } from 'react'
export default function Home() {


    const [seed, setSeed] = useState(1);
       const reset = () => {
            setSeed(Math.random());
        }

    return (
    <div className={styles.main}>
        <Institution reset={reset}/>
        <h1 className={styles['reviews-header']}>Reviews:</h1>
        <Reviews key={seed}/>
     </div> 
    )
}
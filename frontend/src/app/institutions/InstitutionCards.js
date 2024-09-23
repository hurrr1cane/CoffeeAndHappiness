"use client"

import { useEffect, useState } from "react"
import axios from "axios"
import InstitutionCard from "./InstitutionCard"
import styles from './InstitutionCards.module.scss'
export default function InstitutionCards () {

    const [cafes, setCafes] = useState([])

    useEffect(() => {
        axios.get(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/cafe`)
        .then(res => {setCafes(res.data); console.log(res)})
    }, [])

    return (
        <div className={styles.wrapper} style={{height: cafes.length >= 3 ? "auto" : "45.5dvh"}}>
            {cafes.map(cafe => (
                <InstitutionCard cafe={cafe} key={cafe.id}/>
            ))}
        </div>   
    )
}
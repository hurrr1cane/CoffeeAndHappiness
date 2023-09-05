"use client"

import { useEffect, useState } from "react"
import axios from "axios"
import InstitutionCard from "./InstitutionCard"
import styles from './InstitutionCards.module.scss'
export default function InstitutionCards () {

    const [cafes, setCafes] = useState([])

    useEffect(() => {
        axios.get("http://localhost:8080/api/cafe")
        .then(res => {setCafes(res.data); console.log(res)})
    }, [])

    return (
        <div className={styles.wrapper}>
            {cafes.map(cafe => (
                <InstitutionCard cafe={cafe} key={cafe.id}/>
            ))}
        </div>   
    )
}
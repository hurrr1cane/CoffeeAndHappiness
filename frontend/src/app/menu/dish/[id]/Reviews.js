"use client"

import { usePathname } from "next/navigation"
import styles from './reviews.module.scss'
import axios from "axios"
import { useEffect, useState } from "react"

export default function Reviews() {
    const pathname = usePathname().split('dish/')[1]
    const [reviews, setReviews] = useState([])

    useEffect(() => {
        axios.get(`http://localhost:8080/api/food/${pathname}`)
        .then(res => console.log(res.data.reviews))
    }, [pathname])

    return (
        <>
        </>
    )
}
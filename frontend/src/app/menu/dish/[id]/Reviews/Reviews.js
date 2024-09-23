"use client"

import { usePathname } from "next/navigation"
import axios from "axios"
import Review from "./Review"
import { useEffect, useState } from "react"
import styles from './reviews.module.scss'

export default function Reviews() {
    const pathname = usePathname().split('dish/')[1]
    const [reviews, setReviews] = useState([])

    useEffect(() => {
        fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/food/${pathname}`, { cache: 'no-store' })
        .then(response => response.json())
        .then(data => setReviews(data.reviews))
        .catch(error => console.error('Error fetching data:', error));
    }, [pathname, reviews])


    return (
        <div className={styles.wrapper} style={{height: reviews.length >= 3 ? "auto" : "20.5dvh"}}>
        {reviews && reviews.map(review => (
            <Review key={review.id} {...review}/>
        ))}
        </div>
    )
}
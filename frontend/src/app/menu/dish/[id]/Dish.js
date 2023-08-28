"use client"

import { usePathname } from "next/navigation"
import styles from './dish.module.scss'
import axios from "axios"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Rating } from "@mui/material"

export default function Dish() {

    const pathname = usePathname().split('dish/')[1]

    const [dish, setDish] = useState({})

    useEffect(() => {
        axios.get(`http://localhost:8080/api/food/${pathname}`)
        .then(res => setDish(res.data))
        .catch(err => console.log(err))
    }, [])

    return (
            <section className={styles.section}>
                <Image alt="picture of some food" className={styles.image} width={300} height={300} src={dish.imageUrl ?? "/pizza.jpg"}></Image>
                <section className={styles.info}>
                    <h1>{dish.nameEN}</h1>
                    <p>{dish.descriptionEN}</p>
                    <p>Ingredients: {dish.ingredientsEN}</p>
                    <p>Price: {dish.price}</p>
                    <p>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(dish.averageRating)} readOnly/></p>
                </section>
            </section>
    )
}
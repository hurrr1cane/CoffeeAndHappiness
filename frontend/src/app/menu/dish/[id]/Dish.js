"use client"

import { usePathname } from "next/navigation"
import styles from './dish.module.scss'
import axios from "axios"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Rating } from "@mui/material"
import Button from "@mui/material/Button"
import { useGlobalContext } from "@/app/store/store"
import ReviewModal from "./Reviews/ReviewModal"
import useWindowSize from "./Reviews/useWindow"
export default function Dish({ reset }) {
    const {user, setUser, language, setLanguage} = useGlobalContext()
    const pathname = usePathname().split('dish/')[1]

    const { width, height } = useWindowSize()

    const [open, setOpen] = useState(false)

    const [dish, setDish] = useState({})

    useEffect(() => {
        axios.get(`http://localhost:8080/api/food/${pathname}`)
        .then(res => setDish(res.data))
        .catch(err => console.log(err))
    }, [])

    return (
            <section className={styles.section}>
                <ReviewModal width={width} reset={reset} open={open} id={dish.id} token={user.token} setOpen={setOpen}/>
                <Image alt="picture of some food" className={styles.image} width={300} height={300} src={dish.imageUrl ?? "/placeholder.png"}></Image>
                <section className={styles.info}>
                    <h1>{dish.nameEN}</h1>
                    <p>{dish.descriptionEN}</p>
                    <p>Ingredients: {dish.ingredientsEN}</p>
                    <p>Price: {dish.price} ₴</p>
                    <p>Weight : {dish?.weight} g</p>
                    <p>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(dish.averageRating)} readOnly/></p>
                    <div className={styles.buttons} style={{display: user !== {} ? "flex" : "none"}} >
                        <Button onClick={() => {setOpen(true)}} sx={{bgcolor: "#4caf50", '&:hover': {bgcolor:"#66bb69"}}} variant="contained">Add review</Button>
                    </div>
                </section>
            </section>
    )
}
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
    const { user, isDark, language } = useGlobalContext()
    const pathname = usePathname().split('dish/')[1]

    const { width, height } = useWindowSize()

    const [open, setOpen] = useState(false)

    const [dish, setDish] = useState({})

    useEffect(() => {
        axios.get(`https://coffee-and-happiness-backend.azurewebsites.net/api/food/${pathname}`)
        .then(res => setDish(res.data))
        .catch(err => console.log(err))
    }, [])

    return (
            <section className={`${styles.section} ${isDark ? styles.dark : ""}`}>
                <ReviewModal width={width} reset={reset} open={open} id={dish.id} token={user.token} setOpen={setOpen}/>
                <Image alt="picture of some food" className={styles.image} width={300} height={300} src={dish.imageUrl ?? "/placeholder.png"}></Image>
                <section className={styles.info}>
                    <h1>{language === 'en' ? dish.nameEN : dish.nameUA}</h1>
                    <p> {language === 'en' ? dish.descriptionEN : dish.descriptionUA}</p>
                    <p>{language === 'en' ? `Ingredients: ${dish.ingredientsEN}` : `Інгредієнти: ${dish.ingredientsUA}`}</p>
                    <p>{language === 'en' ? 'Price: ' : 'Ціна: '} ₴{dish.price}</p>
                    <p>{language === 'en' ? 'Weight:' : 'Вага: '} {dish?.weight} g</p>
                    <p>{language === 'en' ? 'Rating:' : 'Рейтинг: '} <Rating sx={{top: "5px"}} name="read-only" value={Number(dish.averageRating)} readOnly/></p>
                    <div className={styles.buttons} style={{display: user != {} ? "flex" : "none"}} >
                        {user.token && <Button onClick={() => {setOpen(true)}} sx={{bgcolor: "#4caf50", '&:hover': {bgcolor:"#66bb69"}}} variant="contained">{language === 'en' ? 'Add review' : 'Додати відгук'}</Button>}
                    </div>
                </section>
            </section>
    )
}
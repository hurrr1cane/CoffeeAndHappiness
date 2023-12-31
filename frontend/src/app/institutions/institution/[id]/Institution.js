"use client"

import { usePathname } from "next/navigation"
import styles from './institution.module.scss'
import axios from "axios"
import { useEffect, useState } from "react"
import Image from "next/image"
import { Rating } from "@mui/material"
import Button from "@mui/material/Button"
import { useGlobalContext } from "@/app/store/store"
import ReviewModal from "./Reviews/ReviewModal"
import useWindowSize from "@/app/menu/dish/[id]/Reviews/useWindow"

export default function Institution({ reset }) {

    const { user, isDark, language } = useGlobalContext()
    const pathname = usePathname().split('institution/')[1]
    const [open, setOpen] = useState(false)
    const {width, height } = useWindowSize()
    const [institution, setInstitution] = useState({})

    useEffect(() => {
        axios.get(`https://coffee-and-happiness-backend.azurewebsites.net/api/cafe/${pathname}`)
        .then(res => setInstitution(res.data))
        .catch(err => console.log(err))
    }, [])

    return (
            <section className={`${styles.section} ${isDark ? styles.dark : ""}`}>
                <ReviewModal width={width} reset={reset} open={open} id={institution.id} token={user.token} setOpen={setOpen}/>
                <Image alt="picture of some food" className={styles.image} width={300} height={300} src={institution.imageUrl ?? "/placeholder.png"}></Image>
                <section className={styles.info}>
                    <h1>{language === 'en' ? `Located at ${institution.locationEN}` : `Розташовано в ${institution.locationUA}`} </h1>
                    <p>{language === 'en' ? 'Contact: ' : "Контактні дані: "} {institution.phoneNumber}</p>
                    <p>{language === 'en' ? 'Rating:' : "Рейтинг"} <Rating sx={{top: "5px"}} name="read-only" value={Number(institution.averageRating)} readOnly/></p>
                    <div style={{display: user != {} ? "flex" : "none"}} >
                    {(user.role) && <Button onClick={() => {setOpen(true)}} sx={{marginTop: "1rem", bgcolor: "#4caf50", '&:hover': {bgcolor:"#66bb69"}}} variant="contained">{language === 'en' ? 'Add review' : "Додати відгук"}</Button>}
                    </div>
                </section>
            </section>
    )
}
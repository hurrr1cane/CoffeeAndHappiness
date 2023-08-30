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

export default function Institution() {
    const { user, _ } = useGlobalContext()
    const pathname = usePathname().split('institution/')[1]

    const [open, setOpen] = useState(false)

    const [institution, setInstitution] = useState({})

    useEffect(() => {
        axios.get(`http://localhost:8080/api/cafe/${pathname}`)
        .then(res => setInstitution(res.data))
        .catch(err => console.log(err))
    }, [])

    return (
            <section className={styles.section}>
                <ReviewModal open={open} id={institution.id} token={user.token} setOpen={setOpen}/>
                <Image alt="picture of some food" className={styles.image} width={300} height={300} src={institution.imageUrl ?? "/placeholder.png"}></Image>
                <section className={styles.info}>
                    <h1>Located at {institution.locationEN}</h1>
                    <p>Contact: {institution.phoneNumber}</p>
                    <p>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(institution.averageRating)} readOnly/></p>
                    <div style={{display: user !== {} ? "flex" : "none"}} >
                        <Button onClick={() => {setOpen(true)}} sx={{marginTop: "1rem", bgcolor: "#4caf50", '&:hover': {bgcolor:"#66bb69"}}} variant="contained">Add review</Button>
                    </div>
                </section>
            </section>
    )
}
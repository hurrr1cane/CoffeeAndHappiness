"use client"

import { useEffect, useState } from 'react'
import styles from './review.module.scss'
import axios from 'axios'
import Image from 'next/image'
import { Rating } from '@mui/material'

export default function Review( props ) {

    const [user, setUser] = useState({})

    useEffect(() => {
        axios.get(`http://localhost:8080/api/user/${props?.userId}`)
        .then(res => setUser(res.data))
        .catch(err => console.log(err))
    }, [])



    return (
        <section className={styles['review-card']}>
            <section className={styles.user}>
              <Image alt='user avatar' width={50} height={50} src={user.imageUrl ?? "/user.png"}/>
              <p>
                  {user.firstName} {user.lastName}
              </p>
            </section>
            <section className={styles.comment}>
                <p>{props?.comment}</p> <Rating sx={{bottom: "5px", right: "3px"}} name="read-only" value={Number(props.rating)} readOnly/>
            </section>
        </section>
    )
}
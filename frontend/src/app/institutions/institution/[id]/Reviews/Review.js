"use client"

import { useEffect, useState } from 'react'
import styles from './review.module.scss'
import axios from 'axios'
import Image from 'next/image'
import { Rating } from '@mui/material'
import { Fab } from '@mui/material'
import CloseIcon from '@mui/icons-material'

export default function Review( props ) {

    const [commentUser, setCommentUser] = useState({})

    const { user, _ } = useGlobalContext()

    useEffect(() => {
        axios.get(`http://localhost:8080/api/user/${props?.userId}`)
        .then(res => setCommentUser(res.data))
        .catch(err => console.log(err))
    }, [])

    const handleClick = () => {
        axios.delete(`http://localhost:8080/api/review/cafe/${props.id}`)
        .then(window.location.reload())
        .catch(err => console.log(err))
    }



    return (
        <section className={styles['review-card']}>
            <section className={styles.user}>
              <Image alt='user avatar' width={50} height={50} src={commentUser.imageUrl ?? "/user.png"}/>
              <p>
                  {commentUser.firstName} {commentUser.lastName}
              </p>
            </section>
            <section className={styles.comment}>
                <p>{props?.comment}</p> <Rating sx={{bottom: "5px", right: "3px"}} name="read-only" value={Number(props.rating)} readOnly/>
            </section>
            {((user.id == commentUser.id) || user.role === 'ADMIN') ? <Fab onClick={handleClick} sx={{bgcolor:"#4caf50", "&:hover":{bgcolor:"#4caf50"}, marginLeft:"auto", color:"white "}}><CloseIcon/></Fab> : null}
        </section>
    )
}
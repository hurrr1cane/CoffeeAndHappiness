"use client"

import { useEffect, useState } from 'react'
import styles from './review.module.scss'
import axios from 'axios'
import Image from 'next/image'
import { Rating } from '@mui/material'
import { Fab } from '@mui/material'
import CloseIcon from '@mui/icons-material/Close'
import { useGlobalContext } from '@/app/store/store'
export default function Review( props ) {

    const [commentUser, setCommentUser] = useState({})

    const { user, isDark } = useGlobalContext()

    useEffect(() => {
        axios.get(`https://coffee-and-happiness-backend.azurewebsites.net/api/user/${props?.userId}`)
        .then(res => setCommentUser(res.data))
        .catch(err => console.log(err))
    }, [])

    const handleClick = () => {
        axios.delete(`https://coffee-and-happiness-backend.azurewebsites.net/api/review/cafe/${props.id}`, {
            headers:{
                Authorization: "Bearer " + user.token 
            }
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))
    }



    return (
        <section className={`${styles['review-card']} ${isDark ? styles.dark : ""}`}>
            <section className={styles.user}>
              <Image alt='user avatar' width={70} height={70} style={{borderRadius: "999px", objectFit:"cover"}} src={commentUser.imageUrl ?? "/user.png"}/>
              
            </section>
            <section className={styles.comment}>
                <p>
                  {commentUser.firstName} {commentUser.lastName}
                </p>
                <div style={{display:"flex", flexDirection:"column-reverse"}}>
                    <p>{props?.comment}</p> <Rating sx={{bottom: "5px", right: "3px"}} name="read-only" value={Number(props.rating)} readOnly/>
                </div>
            </section>
            {((user.id == commentUser.id) || user.role === 'ADMIN') ? <Fab onClick={handleClick} sx={{bgcolor:"#4caf50", "&:hover":{bgcolor:"#4caf50"}, marginLeft:"auto", color:"white "}}><CloseIcon/></Fab> : null}
        </section>
    )
}
"use client"

import { useGlobalContext } from "../store/store"
import styles from './user.module.scss'
import Image from "next/image"
import { useRouter } from "next/navigation"
import { useEffect } from "react"
import axios from "axios"
import Orders from "./orders"
import { Pagination } from "@mui/material"


export default function User() {
    const {user, setUser} = useGlobalContext()
    useEffect(() => {
        axios.get(`http://localhost:8080/api/user/email/${user?.email}`)
        .then(res => {
          setUser(
            prev => (
            {
              ...prev,
              firstName: res.data.firstName,
              lastName: res.data.lastName,
              imageUrl: res.data.imageUrl,
              role: res.data.role,
              bonusPoints: res.data.bonusPoints,
              orders: res.data.orders,
              id: res.data.id,
              orders: res.data.orders
            }
              )
          )
        })
        .catch(err => console.log(err)) 
    }, [])
    const { push } = useRouter()
    if (!user) {
        push('/login')
    }
    return (
      <>
      <h1>Welcome, {user?.firstName} {user?.lastName}</h1>
      <div className={styles.container}>
        <section className={styles.main}>
                <div className={styles.user}>
                  <Image className={styles.image} src={user?.imageUrl ? imageUrl : "/user.png"} width={100} height={100} alt="avatar image"/>
                  <h1>{user?.firstName} {user?.lastName}</h1>
                </div>
                <div className={styles.info}>
                    <h2>Bonus points: {user?.bonusPoints}</h2>
                    <h2>Email: {user?.email}</h2>
                </div>
        </section>
        <Orders/>
      </div>
      </>

    )
}
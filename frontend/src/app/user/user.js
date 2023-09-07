"use client"

import { useGlobalContext } from "../store/store"
import styles from './user.module.scss'
import Image from "next/image"
import { useRouter } from "next/navigation"
import { useEffect, useState } from "react"
import axios from "axios"
import Orders from "./orders"
import useWindowSize from "../menu/dish/[id]/Reviews/useWindow"

export default function User() {

    const {user, setUser} = useGlobalContext()

    const { width, height } = useWindowSize()

    useEffect(() => {
      axios.get('https://coffee-and-happiness-backend.azurewebsites.net/api/user/me', {
        headers: {
          Authorization: 'Bearer ' + user.token
        }
      })
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
      {height > 500 ? <h1 style={{fontWeight:400}}>Welcome, {user?.firstName} {user?.lastName}</h1> : <h1>Welcome, <br/>{user?.firstName} {user?.lastName} </h1>}
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
"use client"
import styles from './Dishes.module.scss'
import DishCard from "./DishCard";
import { useEffect, useState } from 'react';
import axios from 'axios';
export default function Dishes({ tab }) {
    const [data, setData] = useState([])

    useEffect(() => {
        axios.get(`https://coffee-and-happiness-backend.azurewebsites.net/api/food/type/${tab}`)
        .then(res => setData(res.data))
        .catch(err => console.log(err))
    }, [tab])

    return (
        <div className={styles.wrapper} style={{height: data.length >= 3 ? "auto" : "45.5dvh"}}>
            {data.map((item, index) => (
                <DishCard key={index} dish = {item} />
            ))}
        </div>
    )
}
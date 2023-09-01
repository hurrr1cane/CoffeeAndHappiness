"use client"
import styles from './Dishes.module.scss'
import DishCard from "./DishCard";
import { useEffect, useState } from 'react';
import axios from 'axios';
export default function Dishes({ tab }) {
    const [data, setData] = useState([])

    useEffect(() => {
        axios.get(`http://localhost:8080/api/food/type/${tab}`)
        .then(res => setData(res.data))
        .catch(err => console.log(err))
    }, [tab])

    return (
        <div className={styles.container}>
            {data.map((item, index) => (
                <DishCard key={index} dish = {item} />
            ))}
        </div>
    )
}
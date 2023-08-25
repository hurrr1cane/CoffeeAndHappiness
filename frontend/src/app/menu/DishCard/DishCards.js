"use client"

import axios from "axios";
import DishCard from "./DishCard";
import { useEffect, useState } from "react";
export default function DishCards() {
    const [dishes, setDishes] = useState()
    useEffect(() => {
        axios.get('http://localhost:8080/api/food')
        .then(res => console.log(res))
    } 
    )  
    return (
        <h1></h1>
    )
}
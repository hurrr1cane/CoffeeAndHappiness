"use client"

import { useGlobalContext } from '@/app/store/store'
import styles from './DishCard.module.scss'
import Image from 'next/image'
import { Rating } from '@mui/material'
import Link from 'next/link'
import { Fab, Stack } from '@mui/material'
import CloseIcon from '@mui/icons-material/Close';
import EditIcon from "@mui/icons-material/Edit"
import axios from 'axios'
import { useState } from 'react'
import EditDishModal from './EditDishModal'

export default function DishCard({dish}) {

    const {user, setUser, language, setLanguage} = useGlobalContext()

    const [open, setOpen] = useState(false)


    const handleDeleteClick = () => {
        axios.delete(`http://localhost:8080/api/food/${dish.id}`, {
            headers: {
                Authorization: "Bearer " + user.token
            }
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))
    }
 

    const handleEditClick = () => {
        setOpen(true)
    }

    return (
        <div className={styles.card}>
                {user.role === 'ADMIN' && <EditDishModal dish={dish} open={open} setOpen={setOpen} id={dish.id} token={user.token}/>}

            <Link href={`/menu/dish/${dish.id}`} className={styles.link}>
                    <Image
                    className={styles.image}
                    src={dish.imageUrl}
                    height={200}
                    width={200}
                    alt='picture of a pizza'
                    />
                    <div>
                        <h1 className={styles.property}>{dish.nameEN}</h1>
                        <p className={styles.property}>Description: {dish.descriptionEN}</p>
                        <p className={styles.property}>Price: {dish.price}</p>
                        <p className={styles.property}>Rating: <Rating sx={{top: "5px"}} name="read-only" value={Number(dish.averageRating)} readOnly /></p>
                    </div>
            </Link>
                    {user.role === 'ADMIN' ? 
                    <Stack direction="row">
                        <Fab onClick={handleDeleteClick} sx={{bgcolor:"#FF0000", "&:hover":{bgcolor:"#FF0000"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><CloseIcon/></Fab>
                        <Fab onClick={handleEditClick} sx={{bgcolor:"#4caf81", "&:hover":{bgcolor:"#4caf81"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><EditIcon/></Fab>
                    </Stack> : ''}
        </div>
    )
}
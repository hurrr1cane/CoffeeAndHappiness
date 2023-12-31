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
import { MuiFileInput } from 'mui-file-input'
import {Button} from '@mui/material'

export default function DishCard({ dish }) {

    const {user, isDark, language} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const [file, setFile] = useState(null)

    const handleFileChange = (newFile) => {
        setFile(newFile);
        console.log(newFile);
      };
    
    const submitImage = () => {
        const formData = new FormData();
        formData.append("image", file);
        axios.post(`https://coffee-and-happiness-backend.azurewebsites.net/api/food/${dish.id}/image/add`, formData, {
          headers: {
            Authorization: "Bearer " + user.token,
            "Content-Type": "multipart/form-data"
          }
        })
        .then(res => console.log(res))
        .catch(err => console.log(err))
        .finally(setFile(null))
      }

    const handleDeleteClick = () => {
        axios.delete(`https://coffee-and-happiness-backend.azurewebsites.net/api/food/${dish.id}`, {
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
        <div className={`${styles.card} ${isDark ? styles.dark : ""}`}>
                {user.role === 'ADMIN' && <EditDishModal dish={dish} open={open} setOpen={setOpen} id={dish.id} token={user.token}/>}

            <div>
                <Link href={`/menu/dish/${dish.id}`} className={styles.link}>
                        <Image
                        className={styles.image}
                        src={dish?.imageUrl || "/placeholder.png"}
                        height={200}
                        width={200}
                        alt='picture of a pizza'
                        />
                        <div>
                            <h1 className={styles.property}>{language === 'en' ? dish.nameEN : dish.nameUA}</h1>
                            <p className={styles.property}>{language === 'en' ? 'Description:' : 'Опис:'} {language === 'en' ? dish.descriptionEN : dish.descriptionUA}</p>
                            <p className={styles.property}>{language === 'en' ? 'Price: ' : 'Ціна: '} ₴{dish.price}</p>
                            <p className={styles.property}>{language === 'en' ? 'Rating: ' : 'Рейтинг: '} <Rating sx={{top: "5px"}} name="read-only" value={Number(dish.averageRating)} readOnly /></p>
                        </div>
                </Link>
                {user.role === 'ADMIN' && <Stack direction="column">
                <MuiFileInput value={file} fullWidth placeholder="Upload image here..." sx={{mb: 1, marginLeft:"1rem"}} onChange={handleFileChange}/>
                        {file && <Button
                variant="contained"
                fullWidth
                onClick={submitImage}
                sx={{
                    marginBottom: 1,
                    marginLeft: "1rem",
                    color: isDark ? '#CCCCCC' : "white",
                    bgcolor: isDark ? "#388E3C" : "#4caf50",
                    "&:hover": { bgcolor: isDark ? "#388E3C" : "#4caf50" },
                }}
                >
                Submit image
                </Button> 
                }   
                    </Stack>}
            </div>
                    {user.role === 'ADMIN' ? 
                    <Stack direction="row">
                        <Fab onClick={handleDeleteClick} sx={{bgcolor:"#FF0000", "&:hover":{bgcolor:"#FF0000"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><CloseIcon/></Fab>
                        <Fab onClick={handleEditClick} sx={{bgcolor:"#4caf81", "&:hover":{bgcolor:"#4caf81"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><EditIcon/></Fab>
                    </Stack> : ''}
        </div>
    )
}
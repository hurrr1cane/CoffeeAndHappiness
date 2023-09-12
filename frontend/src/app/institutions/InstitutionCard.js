"use client"

import styles from './InstitutionCard.module.scss'
import Image from 'next/image'
import Link from 'next/link'
import { useGlobalContext } from '../store/store'
import { useState } from 'react'
import {Fab, Stack} from "@mui/material"
import { Edit } from '@mui/icons-material'
import { Close } from '@mui/icons-material'
import EditCafeModal from './EditCafeModal'
import Button from '@mui/material/Button'
import useWindowSize from '../menu/dish/[id]/Reviews/useWindow'
export default function InstitutionCard({ cafe }) {

    const {user, isDark, language} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const { width, height } = useWindowSize()

    const handleDeleteClick = () => {
        axios.delete(`https://coffee-and-happiness-backend.azurewebsites.net/api//${cafe.id}`, {
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
        {user.role === 'ADMIN' && (
          <EditCafeModal
            cafe={cafe}
            open={open}
            setOpen={setOpen}
            id={cafe.id}
            token={user.token}
          />
        )}
      
          <Image
            src={cafe?.imageUrl}
            className={styles.image}
            alt={'image of a restaurant'}
            height={200}
            width={200}
          />
          <div>
            <h1 className={styles.property}>{language === 'en' ? cafe.locationEN : cafe.locationUA}</h1>
            <p className={styles.property}>{language === 'en' ? "Phone number" : "Номер телефону"} {cafe.phoneNumber}</p>
            <Button variant='contained'  fullWidth  sx={{marginBottom:1, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"}}}>
              <p>
                <Link href="https://www.google.com/maps">{language === 'en' ? "View on a map" : "Переглянути на карті"}</Link>
              </p>
            </Button>
            <Button variant='contained'  fullWidth    sx={{marginBottom:1,  bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"}}}>
              <p>
                <Link href={`institutions/institution/${cafe?.id}`}>{language === 'en' ? "View reviews" : "Переглянути відгуки"}</Link>
              </p>
            </Button>
          </div>
        {user.role === 'ADMIN' && (
          <div className={styles.adminActions}>
            <Fab onClick={handleDeleteClick} className={styles.fab} sx={{bgcolor:"#FF0000", "&:hover":{bgcolor:"#FF0000"}, m:1}}>
              <Close />
            </Fab>
            <Fab onClick={handleEditClick} className={styles.fab} sx={{bgcolor:"#4caf81", "&:hover":{bgcolor:"#4caf81"},  m:1}}>
              <Edit />
            </Fab>
          </div>
        )}
      </div>      
    )
}
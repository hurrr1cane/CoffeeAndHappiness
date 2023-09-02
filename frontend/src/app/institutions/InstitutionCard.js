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
export default function InstitutionCard({ cafe }) {

    const {user, _} = useGlobalContext()

    const [open, setOpen] = useState(false)

    const handleDeleteClick = () => {
        axios.delete(`http://localhost:8080/api//${cafe.id}`, {
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
        {user.role === 'ADMIN' && <EditCafeModal cafe={cafe} open={open} setOpen={setOpen} id={cafe.id} token={user.token}/>}

        <Image
        src={cafe?.imageUrl}
        className={styles.image}
        alt={'image of a restaurant'}
        height={200}
        width={200}
        />
        <div className={styles.description}>
            <p>{cafe?.locationEN}</p>
            <p>Phone number: {cafe.phoneNumber}</p>
            <p>
                <Link href="https://www.google.com/maps">View on a map</Link>
            </p>
            <p>
                <Link href={`institutions/institution/${cafe?.id}`}>View reviews</Link>
            </p>
        </div>  
        {user.role === 'ADMIN' ? 
                    <Stack direction="row" sx={{marginLeft:"auto"}}>
                        <Fab onClick={handleDeleteClick} sx={{bgcolor:"#FF0000", "&:hover":{bgcolor:"#FF0000"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><Close/></Fab>
                        <Fab onClick={handleEditClick} sx={{bgcolor:"#4caf81", "&:hover":{bgcolor:"#4caf81"}, marginLeft:"auto", color:"white", marginRight:"1rem"}}><Edit/></Fab>
                    </Stack> : ''}
    </div>
    )
}
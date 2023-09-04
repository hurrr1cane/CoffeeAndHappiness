"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import EmailOutlinedIcon from '@mui/icons-material/EmailOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { useForm } from 'react-hook-form'
import axios from 'axios';
import { useState } from 'react';
import { NextLink } from 'next/link'
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import styles from './page.module.scss'
import { MuiOtpInput } from 'mui-one-time-password-input'
import KeyOutlinedIcon from '@mui/icons-material/KeyOutlined';
import PasswordOutlinedIcon from '@mui/icons-material/PasswordOutlined';
export default function Home() {

    const { push } = useRouter();
    const { handleSubmit, register, formState: { errors } } = useForm();

    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);

    const [error, setError] = useState("")
    const [showAlert, setShowAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)
    const [showOtpAlert, setShowOtpAlert] = useState(false)

    const [showEmailInput, setShowEmailInput] = useState(true)
    const [showOtpInput, setShowOtpInput] = useState(false)
    const [showPasswordInput, setShowPasswordInput] = useState(false)
    const [showButton, setShowButton] = useState(false)


    const [email, setEmail] = useState('')

    const [otp, setOtp] = useState('')

    const handleChange = (newValue) => {
        setOtp(newValue)
    }


    const onEmailSubmit = (data) => {
        setEmail(data.email)
        console.log(data)
        setShowSuccessAlert(true)
        setTimeout(() => {setShowEmailInput(false)
        setShowOtpInput(true); setShowSuccessAlert(false)}, 1000)
    }

    const handleOtpSubmit = (value) => {    
        console.log(value)
        setShowOtpAlert(true)
        
        setTimeout(() => {
            setShowOtpInput(false)
            setShowOtpAlert(false)
            setShowPasswordInput(true)
            setShowButton(false)
        }, 1000)
        
    }

    const onPasswordSubmit = (data) => {
        console.log(data)
    }

    return (
        <div className={styles.main}>
            <Container component="main" maxWidth="xs">
              <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8, marginBottom: 1}}>Password reset code sent.</Alert>
              <Alert severity='success' onClose={() => {setShowOtpAlert(false)}} sx={{display: showOtpAlert ? "flex" : "none", marginTop:8, marginBottom: 1}}>Code validated successfully.</Alert>  
              <Alert onClose={() => {setShowAlert(false)}} sx={{display: showAlert ? "flex" : "none"}}
               severity="error">{error}</Alert>
                <Box
                  sx={{
                    marginTop: 2,
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                  }}
                >

                {showOtpInput ? 
                (<>
                    <Avatar sx={{ m: 1, bgcolor: '#4caf50' }}>
                        <KeyOutlinedIcon />
                        </Avatar>
                        <Typography sx={{whiteSpace:"nowrap", p:1}} component="h1" variant="h5">
                        Input the code sent to {email}
                        </Typography>
                        <MuiOtpInput length={6} onComplete={() => setShowButton(true)} value={otp} onChange={handleChange} />
                        <Button
                            onClick={handleOtpSubmit}
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, bgcolor:"#4caf50", '&:hover':{bgcolor:"#4caf50 "}, display: showButton ? "block" : "none" }}
                        >
                            Submit password reset code
                        </Button>
                </>) : null
                     
                }


                {showPasswordInput ?
                    (   <>
                        <Avatar sx={{ m: 1, bgcolor: '#4caf50' }}>
                        <PasswordOutlinedIcon />
                        </Avatar>
                        <Typography sx={{whiteSpace:"nowrap", p:1}} component="h1" variant="h5">
                        Input new password
                        </Typography>
                        <Box component="form" onSubmit={handleSubmit(onPasswordSubmit)} noValidate sx={{ mt: 1 }}>
                        <TextField
                        color="success"
                        {...register('password', {
                            required: 'Password is required',
                        })}
                        margin="normal"
                        required
                        fullWidth
                        name="password"
                        label="Password"
                        type={showPassword ? "text" : "password"}
                        sx = {{
                            fieldset: {
                                outlineColor: "red"
                            }
                        }}
                        InputProps={{ 
                            endAdornment: (
                            <InputAdornment position="end">
                                <IconButton
                                aria-label="toggle password visibility"
                                onClick={handleClickShowPassword}
                    
                                >
                                {showPassword ? <Visibility /> : <VisibilityOff />}
                                </IconButton>
                            </InputAdornment>)}}
                        id="password"
                        autoComplete="current-password"
                        error={!!errors.password}
                        helperText={errors.password?.message}
                        />
                         <Button
                            type="submit"                  
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, bgcolor:"#4caf50", '&:hover':{bgcolor:"#4caf50 "}}}
                        >
                            Submit password reset code
                        </Button>
                        </Box>
                        </>
                    )
                    : null
                }

                {showEmailInput ? (
                    <>
                        <Avatar sx={{ m: 1, bgcolor: '#4caf50' }}>
                        <EmailOutlinedIcon />
                        </Avatar>
                        <Typography component="h1" variant="h5">
                        Reset password
                        </Typography>
                        <Box component="form" onSubmit={handleSubmit(onEmailSubmit)} noValidate sx={{ mt: 1 }}>
                        <TextField
                            color="success"
                            {...register('email', {
                            required: 'Email address is required',
                            pattern: {
                                value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                                message: 'Invalid email address'
                            }
                            })}
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            autoFocus
                            error={!!errors.email}
                            helperText={errors.email?.message}
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2, bgcolor:"#4caf50", '&:hover':{bgcolor:"#4caf50 "} }}
                        >
                            Send reset password code
                        </Button>
                        <Grid container>
                            <Grid item xs>
                            </Grid>
                            <Grid item>
                            <Link component={NextLink} href='/login' variant='body2'>
                                Already have an account? Log in
                            </Link>
                            </Grid>
                        </Grid>
                        </Box>
                    </>
                    ) : null}
                </Box>
            </Container>
        </div>
    )
}
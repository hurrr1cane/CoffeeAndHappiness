"use client"

import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import { useForm } from 'react-hook-form'
import { Container } from '@mui/material';
import axios from 'axios'
import { useState } from 'react';
import { IconButton, InputAdornment } from '@mui/material';
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import NextLink from 'next/link'
import { useGlobalContext } from '../store/store';
import { useRouter } from 'next/navigation';
import { Alert, AlertTitle } from '@mui/material';

export default function Register() {

    const { push } = useRouter();
    const {isDark, setUser, language} = useGlobalContext()
    const [showPassword, setShowPassword] = useState(false);
    const handleClickShowPassword = () => setShowPassword(!showPassword);
    const { handleSubmit, register, formState: { errors } } = useForm();
    const [errorMessage, setErrorMessage] = useState('');
    const [showErrorAlert, setShowErrorAlert] = useState(false)
    const [showSuccessAlert, setShowSuccessAlert] = useState(false)
    const onSubmit = (data) => {
        setUser({
          email: data.email
        })
        axios.post('https://coffee-and-happiness-backend.azurewebsites.net/api/auth/register', {
            firstName: data.firstName,
            lastName: data.lastName,
            email: data.email,
            password: data.password,
            role: 'USER'
        })
        .then(res => {
          setUser(prev => ({...prev, token: res.data.accessToken, refreshToken: res.data.refreshToken, date: Date.now()}))
          setShowSuccessAlert(true)
          setTimeout(() => {
            push('/user')
          }, 1000) 
        })
        .catch(err => {setErrorMessage(err.response.data.errorMessage); setShowErrorAlert(true)})

        
    };
    return (
    <Container component="main" maxWidth="xs">
      <Alert severity='success' onClose={() => {setShowSuccessAlert(false)}} sx={{display: showSuccessAlert ? "flex" : "none", marginTop:8}}><AlertTitle>Account registered successfully!</AlertTitle> You will be redirected shortly.</Alert>
      <Alert  severity="error" onClose={() => {setShowErrorAlert(false)}} sx={{display: showErrorAlert ? "flex" : "none"}}>{errorMessage}</Alert>
        <Box
            sx={{
              marginTop: 1,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Avatar sx={{ m: 1, bgcolor:isDark ? "#6b6b6b" : '#4caf50'  }}>
              <LockOutlinedIcon />
            </Avatar>
            <Typography component="h1" variant="h5" sx={{color: isDark && "#CCCCCC"}}>
            {language === 'en' ? 'Register' : "Реєстрація"}
            </Typography>
            <Box component="form" onSubmit={handleSubmit(onSubmit)} noValidate>
              <TextField
                {...register('firstName', { required: language === 'en' ? 'First name is required' : "Ім'я обов'язкове"})}
                color="success"
                margin="normal"
                required
                fullWidth
                name="firstName"
                label={language === 'en' ? 'First name' : "Ім'я"}
                id="firstName"
                error={!!errors.firstName}
                helperText={errors.firstName?.message}
                sx={{
                  input : {
                    color: isDark && "#CCCCCC",
                  }
                }}
              />
              <TextField
                {...register('lastName', { required: language === 'en' ? 'Last name is required' : "Прізвище обов'язкове" })}
                color="success"
                margin="normal"
                required
                fullWidth
                name="lastName"
                label={language === 'en' ? 'Last name' : "Прізвище"}
                id="lastName"
                error={!!errors.lastName}
                helperText={errors.lastName?.message}
                sx={{
                  input : {
                    color: isDark && "#CCCCCC",
                  }
                }}
              />
              <TextField
                {...register('email', {
                  required: language === 'en' ? 'Email address is required' : "Електронна пошта обов'язкова",
                  pattern: {
                    value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i,
                    message: language === 'en' ? 'Invalid email address' : 'Неправильна електронна пошта'
                  }
                })}
                color="success"
                margin="normal"
                required
                fullWidth
                id="email"
                label={language === 'en' ? 'Email adress' : "Електронна пошта"}
                name="email"
                autoComplete="email"
                autoFocus
                error={!!errors.email}
                helperText={errors.email?.message}
                sx={{
                  input : {
                    color: isDark && "#CCCCCC",
                  }
                }}
              />
              <TextField
                {...register('password', {
                  required: language === 'en' ? 'Password is required' : "Пароль обов'язковий",
                  minLength: {
                    value: 8,
                    message: language === 'en' ? 'Password must be at least 8 characters long' : "Пароль мусить мати хоча б 8 символів"
                  }
                })}
                color="success"
                margin="normal"
                required
                fullWidth
                name="password"
                label={language === 'en' ? 'Password' : "Пароль"}
                type={showPassword ? "text" : "password"}
                sx={{
                  input : {
                    color: isDark && "#CCCCCC",
                  }
                }}
                InputProps={{ 
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={handleClickShowPassword}

                      >
                        {showPassword ? <Visibility sx={{color: isDark && "#CCCCCC"}}/> : <VisibilityOff sx={{color: isDark && "#CCCCCC"}}/>}
                      </IconButton>
                    </InputAdornment>)
                }}
                id="password"
                autoComplete="current-password"
                error={!!errors.password}
                helperText={errors.password?.message}
              />
              <Typography component="h1" variant="h5" color="error">
                {errorMessage}
              </Typography>
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2, bgcolor:isDark ? "#388E3C" : "#4caf50", '&:hover':{bgcolor:isDark ? "#388E3C" : "#4caf50"} }}              >
                {language === 'en' ? 'Register' : "Зареєструватися"}
              </Button>
              <Grid container>
                <Grid item xs>
                </Grid>
                <Grid sx={{color: isDark && "#CCCCCC"}} item>
                  <Link component={NextLink} href='/login' variant="body2">
                  {language === 'en' ? 'Already have an account? Log in' : "Вже маєте аккаунт? Увійти"}
                  </Link>
                </Grid>
              </Grid>
            </Box>
          </Box>
      </Container>
    )
}
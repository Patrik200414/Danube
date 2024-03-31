import React from 'react'
import ReactDOM from 'react-dom/client'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom';
import Login from './page/Login';
import Registration from './page/Registration';
import Home from './page/Home';
import Profile from './page/Profile';
import SellerAgreement from './page/SellerAgreement';
import Verification from './page/Verification';
import ProductUpload from './page/ProductUpload';
import UserUpdate from './page/UserUpdate';
import PasswordUpdate from './page/PasswordUpdate';

const verificationToPages = ['upload', 'update'];

const router = createBrowserRouter([
  {
    path: '/',
    element: <Home />
  },
  {
    path: '/login',
    element: <Login />
  },
  {
    path: '/registration',
    element: <Registration />
  },
  {
    path: '/profile',
    element: <Profile />
  },
  {
    path: '/seller/agreement',
    element: <SellerAgreement />
  },
  {
    path: '/verification/:verificationBy/:to',
    element: <Verification verificationToPages={verificationToPages}/>
  },
  {
    path: '/product/upload',
    element: <ProductUpload />
  },
  {
    path: '/user/update',
    element: <UserUpdate />
  },
  {
    path: '/user/update/password',
    element: <PasswordUpdate />
  }
])


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)

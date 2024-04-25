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
import NavBar from './component/NavBar';
import Item from './page/Item';
import NotFound from './page/NotFound';

const verificationToPages = ['upload', 'update'];

const router = createBrowserRouter([
  {
    path: '/',
    element: <NavBar />,
    children: [
      {
        path: '',
        element: <Home />
      },
      {
        path: 'profile',
        element: <Profile />
      },
      {
        path: 'seller/agreement',
        element: <SellerAgreement />
      },
      {
        path: 'product/upload',
        element: <ProductUpload />
      },
      {
        path: '/user/update',
        element: <UserUpdate />
      },
      {
        path: '/user/update/password',
        element: <PasswordUpdate />
      },
      {
        path: '/item/:id',
        element: <Item />
      },
      {
        path: '*',
        element: <NotFound />
      }
    ]
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
    path: '/verification/:verificationBy/:to',
    element: <Verification verificationToPages={verificationToPages}/>
  },
])


ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>,
)

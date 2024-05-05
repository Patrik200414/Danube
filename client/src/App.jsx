
import { RouterProvider } from "react-router-dom"
import { NavbarProvider } from "./NavbarContext"
import  { useState } from 'react'
import './index.css'
import { createBrowserRouter } from 'react-router-dom';
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
import ProductDashBoard from './page/ProductDashBoard';
import ProductUpdate from './page/ProductUpdate';
import getNavbarInformation from './utility/getNavbarInformation';

function App() {
  const [navbarInformation, setNavbarInformation] = useState(getNavbarInformation(JSON.parse(sessionStorage.getItem('USER_JWT'))));

  const verificationToPages = ['upload', 'update', 'password'];


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
          element: <UserUpdate onNavbarInformationChange={(information) => setNavbarInformation(information)}/>
        },
        {
          path: '/user/password',
          element: <PasswordUpdate />
        },
        {
          path: '/item/:id',
          element: <Item />
        },
        {
          path: '/product/update',
          element: <ProductDashBoard />
          
        },
        {
          path: '/update/:productId',
          element: <ProductUpdate />
        },
        {
          path: '*',
          element: <NotFound />
        }
      ]
    },
    {
      path: '/login',
      element: <Login onNavbarInformationChange={(information) => setNavbarInformation(information)}/>
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

  return (
    <>
      <NavbarProvider navbarInformation={navbarInformation}>
        <RouterProvider router={router} />
      </NavbarProvider>
    </>
  )
}


export default App;

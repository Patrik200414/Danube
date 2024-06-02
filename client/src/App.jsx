
import { RouterProvider } from "react-router-dom"
import { NavbarProvider } from "./NavbarContext"
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
import useGetNavbarInformation from './utility/customHook/useGetNavbarInformation';
import MyCart from "./page/MyCart";
import Checkout from "./page/Checkout";
import PaymentSuccess from "./page/PaymentSuccess";

function App() {
  const [navbarInformation, setNavbarInformation] = useGetNavbarInformation(JSON.parse(sessionStorage.getItem('USER_JWT')));

  const verificationToPages = ['upload', 'update', 'password', 'checkout'];


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
          element: <Item onNavbarInformationChange={(information) => setNavbarInformation(information)}/>
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
          path: '/cart',
          element: <MyCart onNavbarInformationChange={(information) => setNavbarInformation(information)}/>
        },
        {
          path: '*',
          element: <NotFound />
        },
        {
          path: '/user/checkout',
          element: <Checkout />
        },
        {
          path: '/payment/success',
          element: <PaymentSuccess onNavbarInformationChange={(information) => setNavbarInformation(information)}/>
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

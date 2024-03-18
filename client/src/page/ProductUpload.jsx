import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import ProductDetailsForm from "../component/ProductDetailsForm"
import { useNavigate } from "react-router-dom";

function ProductUpload(){
    const [user, setUser] = useState();
    const [productDetail, setProductDetail] = useState({
        productName: '',
        brand: '',
        price: '',
        shippingPrice: '',
        quantity: '',
        deliveryTimeInDate: '',
        description: ''
    });

    const navigate = useNavigate();

    useEffect(() => {
        const userData = JSON.parse(sessionStorage.getItem('USER_JWT'));

        if(!userData){
            navigate('/');
            return;
        }

        if(!userData.roles.includes('ROLE_SELLER')){
            navigate('/profile');
            return;
        }

        setUser(userData);
    }, []);

    function handleDetailChange(value, field){
        const newDetail = {
            ...productDetail
        }
        newDetail[field] = value;

        setProductDetail(newDetail);
    }

    return(
        <div className="product-upload">
            {user && 
                <>
                    <NavBar />
                    <ProductDetailsForm onDetailsChange={handleDetailChange} productDetail={productDetail}/>
                </>
            }
        </div>
    )
}

export default ProductUpload;
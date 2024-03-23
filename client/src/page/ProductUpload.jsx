import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import ProductDetailsForm from "../component/ProductDetailsForm"
import { useNavigate } from "react-router-dom";
import ProductCategoryForm from "../component/ProductCategoryForm";

function ProductUpload(){
    const [user, setUser] = useState();
    const [productDetail, setProductDetail] = useState();


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
        setProductDetail({
            productName: '',
            brand: '',
            price: '',
            shippingPrice: '',
            quantity: '',
            deliveryTimeInDay: '',
            description: '',
            userId: userData.id
        })
    }, []);

    function handleDetailChange(value, field){
        const newDetail = {
            ...productDetail
        }
        newDetail[field] = value;

        setProductDetail(newDetail);
    }

    async function handleSubmit(e){
        e.preventDefault();
        console.log(productDetail);
    }

    return(
        <div className="product-upload">
            {user && 
                <>
                    <NavBar />
                    <ProductDetailsForm onDetailsChange={handleDetailChange} productDetail={productDetail}/>
                    <ProductCategoryForm />
                    {}
                    <button onClick={handleSubmit} type="button" >Upload product!</button>
                </>
            }
        </div>
    )
}

export default ProductUpload;
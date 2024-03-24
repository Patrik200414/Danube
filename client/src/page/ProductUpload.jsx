import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import ProductInformationForm from "../component/ProductInformationForm"
import { useNavigate } from "react-router-dom";
import ProductCategoryForm from "../component/ProductCategoryForm";
import ProductDetailsForm from "../component/ProductDetailsForm";

function ProductUpload(){
    const [user, setUser] = useState();
    const [product, setProduct] = useState();
    const [details, setDetails] = useState();


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
        setProduct({
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

    function handleProductChange(value, field){
        const newDetail = {
            ...product
        }
        newDetail[field] = value;

        setProduct(newDetail);
    }

    function handleDetailChange(detailName, value){
        setDetails(prev => ({
            ...prev,
            [detailName]: value
        }));
    }

    async function handleSubmit(e){
        e.preventDefault();
        console.log(product);
        console.log(details);
    }

    return(
        <div className="product-upload">
            {user && 
                <>
                    <NavBar />
                    <ProductInformationForm onDetailsChange={handleProductChange} productDetail={product}/>
                    <ProductCategoryForm user={user} onDetailSet={(detail) => setDetails(detail)}/>
                    {details &&
                        <ProductDetailsForm details={details} onDetailsChange={handleDetailChange}/>
                    }
                    <button onClick={handleSubmit} type="button" >Upload product!</button>
                </>
            }
        </div>
    )
}

export default ProductUpload;
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
    const [error, setError] = useState();


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
            'Product name': '',
            'Brand': '',
            'Price': '',
            'Shipping price': '',
            'Quantity': '',
            'Delivery time in day': '',
            'Description': '',
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
        
        const errorFields = [
            ...formDataValidator(product),
            ...formDataValidator(details)
        ].join(', ');

        console.log(errorFields);
        const errors = `Missing value at: ${errorFields}!`;

        setError(errors);
    }
    

    function formDataValidator(information){
        const missingFields = [];
        console.log(information);
        for(const informationKey in information){
            if(!information[informationKey]){
                missingFields.push(informationKey);
            }
        }

        return missingFields;
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
                    <p className="error-message">{error}</p>
                    <button onClick={handleSubmit} type="button" >Upload product!</button>
                </>
            }
        </div>
    )
}

export default ProductUpload;
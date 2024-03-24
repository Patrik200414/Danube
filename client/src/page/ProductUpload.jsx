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
    const [productCategories, setProductCategories] = useState({
        'Category': '',
        'Subcategory': ''
    });
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
            ...formDataValidator(productCategories),
        ].join(', ');

        if(errorFields.length > 0){
            const errors = `Missing value at: ${errorFields}!`;
            setError(errors);
            return
        } else{
            setError();
        }


        const convertedProduct = fieldNameConverter(product);

        const newProduct = {
            productDetail: convertedProduct,
            productInformation: details,
            userId: user.id
        }

        const uploadProductResponse = await fetch('/api/product', {
            method: 'POST',
            headers: {
                'Content-type': 'Application/json',
                'Authorization': `Bearer ${user.jwt}`
            },
            body: JSON.stringify(newProduct)
        });

        console.log(uploadProductResponse);

    }
    

    function fieldNameConverter(information){
        const convertedInformations = {};
        for(const key in information){
            const splitted = key.split(' '); 
            if(splitted.length > 1){
                const firstPart = splitted[0].toLowerCase();
                const restPart = splitted.slice(1).map(propertyName => propertyName[0].toUpperCase() + propertyName.slice(1).toLowerCase()).join('');
                convertedInformations[firstPart + restPart] = information[key];
            } else {
                const firstPart = splitted[0].toLowerCase();
                convertedInformations[firstPart] = information[key];
            }
        }

        return convertedInformations;
    }

    function formDataValidator(information){
        const missingFields = [];
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
                    <ProductCategoryForm user={user} onDetailSet={(detail) => setDetails(detail)} onCategoryChange={(categories) => setProductCategories(categories)}/>
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
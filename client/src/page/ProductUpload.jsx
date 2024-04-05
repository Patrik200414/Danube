import { useEffect, useState } from "react";
import NavBar from "../component/NavBar";
import ProductInformationForm from "../component/ProductInformationForm"
import { useNavigate } from "react-router-dom";
import ProductCategoryForm from "../component/ProductCategoryForm";
import ProductDetailsForm from "../component/ProductDetailsForm";
import UploadedImages from "../component/UploadedImages";

function ProductUpload(){
    const SUCCESS_MESSAGE_TIME_IN_SECONDS = 2;

    const [user, setUser] = useState();
    const [product, setProduct] = useState();
    const [details, setDetails] = useState();
    const [images, setImages] = useState([]);

    const [selectedCategoryId, setSelectedCaegoryId] = useState();
    const [selectedSubcategoryId, setSelectedSubcategoryId] = useState();

    const [error, setError] = useState();
    const [successUploadCount, setSuccessUploadCount] = useState(0);
    const [successMessage, setSuccessMessage] = useState('');


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

    useEffect(() => {
        if(successUploadCount){
            let currTime = SUCCESS_MESSAGE_TIME_IN_SECONDS;
            setSuccessMessage('The upload of the product was successfull!');
            const interval = setInterval(() => {
                if(currTime === 1){
                    clearInterval(interval);
                    setSuccessMessage('');
                }
                currTime--;
            }, 1000);
        }
    }, [successUploadCount]);


    function handleProductChange(value, field){
        const newDetail = {
            ...product
        }
        newDetail[field] = value;

        setProduct(newDetail);
    }

    function handleDetailChange(value, detailId){
        const changedDetails = details.map(detail => {
            if(detail.id === detailId){
                return{
                    ...detail,
                    value: value
                }
            }
            return detail;
        });
        setDetails(changedDetails);
    }

    function handleImagesChange(e){
        const uploadedFiles = e.target.files;
        const files = [];
        for(const fileIndex in uploadedFiles){
            const isImageAlreadyAdded = images.filter(image => image.name === uploadedFiles[fileIndex].name).length > 0;
            if(typeof uploadedFiles[fileIndex] === 'object' && !isImageAlreadyAdded){
                files.push(uploadedFiles[fileIndex]);
            }
        }

        setImages(prev => [...prev, ...files]);
    }

    async function handleSubmit(e){
        e.preventDefault();
        
        
        const convertedDetails = details ? detailsConverter(details) : [];
        const errorFields = [
            ...formDataValidator(product),
            ...formDataValidator({
                Category: selectedCategoryId,
                Subcategory: selectedSubcategoryId
            }),
            ...formDataValidator(convertedDetails)
        ].join(', ');

        if(isCorrectForm(errorFields)){
            const convertedProduct = fieldNameConverter(product);
            
            
            const formData = createFormData(convertedProduct, convertedDetails);
            

            const uploadProductResponse = await fetch('/api/product', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${user.jwt}`
                },
                body: formData
            });
    
            if(uploadProductResponse.ok){
                setProduct({
                    'Product name': '',
                    'Brand': '',
                    'Price': '',
                    'Shipping price': '',
                    'Quantity': '',
                    'Delivery time in day': '',
                    'Description': '',
                });
                setSelectedCaegoryId('');
                setSelectedSubcategoryId('');
                setDetails();
                setImages([]);
                setSuccessUploadCount(prev => prev + 1);
            } else{
                const uploadResponse = await uploadProductResponse.json();
                setError(uploadResponse.errorMessage);
            }
        }

    }

    function createFormData(product, details){
            const formData = new FormData();
            formData.append('productDetail', JSON.stringify(product));
            formData.append('productInformation', JSON.stringify(details));
            formData.append('userId', user.id);
            
            appendFilesToFormData(formData, images);

            return(formData)
    }

    function appendFilesToFormData(formData, files){
        for(let i = 0; i < files.length; i++){
            formData.append(`image`, files[i]);
        }

        return formData;
    }

    function isCorrectForm(errorFields){
        if(errorFields.length > 0){
            const errors = `Missing value at: ${errorFields}!`;
            setError(errors);
            return false;
        } else{
            setError();
            return true;
        }
    }


    function detailsConverter(productDetails){
        const convertedProductDetails = {};
        productDetails.forEach(productDetail => convertedProductDetails[productDetail.detailName] = productDetail.value);
        return convertedProductDetails;
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
                    {successMessage && <h3 className="success-message">{successMessage}</h3>}
                    <ProductInformationForm onDetailsChange={handleProductChange} productDetail={product}/>
                    <ProductCategoryForm 
                        onSelectCategoryIdChange={(categoryId) => setSelectedCaegoryId(categoryId)} 
                        onSelectedSubCategoryIdChange={(subcategoryId) => setSelectedSubcategoryId(subcategoryId)} 
                        selectedCategoryId={selectedCategoryId}
                        selectedSubcategoryId={selectedSubcategoryId}
                    />
                    {selectedSubcategoryId &&
                        <ProductDetailsForm 
                            details={details}
                            onDetailsChange={handleDetailChange}
                            subCategoryId={selectedSubcategoryId} 
                            user={user}
                            onDetailsSet={(details) => setDetails(details)}
                            onImageUpload={handleImagesChange}
                        />
                    }
                    {images.length && <UploadedImages images={images}/>}
                    <p className="error-message">{error}</p>
                    <button className="submit-button" onClick={handleSubmit} type="button" >Upload product!</button>
                </>
            }
        </div>
    )
}

export default ProductUpload;
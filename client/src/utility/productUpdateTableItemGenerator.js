function productUpdateTableItemGenerator(product){
    return {
        Productimage: product.images[0],
        'Product name': product.productName,
        Owner: product.sellerName,
        id: product.id
    }
}

export default productUpdateTableItemGenerator;
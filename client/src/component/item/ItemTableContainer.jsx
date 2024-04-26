import ItemTable from "./ItemTable";

function ItemTableContainer({tableName, renderElement}){
    return(
        <div className="item-table-container">
            <h3>{tableName}: </h3>
            <ItemTable renderElement={renderElement}/>
        </div>
    )
}

export default ItemTableContainer;
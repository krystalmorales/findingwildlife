import { useContext, useState } from "react";
import { useHistory } from "react-router-dom";
import UserContext from "../UserContext";
import Error from "./Error";

const DEFAULT_ORGANISM = {commonName: '', scientificName: '', category: ''};

function OrganismAddForm(){
    const [organism, setOrganism] = useState(DEFAULT_ORGANISM);
    const [errors, setErrors] = useState([]);

    const history = useHistory();
    const authManager = useContext(UserContext);

    function addOrganism() {
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify({...organism})
        };

        fetch('http://localhost:8080/finding-wildlife/organism', init)
        .then(response => {
            if(response.status === 201 || response.status === 400){
                return response.json();
            }
            return Promise.reject('Something Went Wrong!');
        })
        .then(body => {
            if(body.organismId){
                history.push('/admin')
            }
            else if(body){
                setErrors(body);
            }
        })
        .catch(error => history.push('/error', {errorMessage: error}));
    };

    function onSubmit(event){
        event.preventDefault();

        const fetchFunction = addOrganism;

        fetchFunction();
    }

    function handleChange(event){
        const property = event.target.name;
        const value = event.target.value;

        const newOrganism = {...organism};

        newOrganism[property] = value;
        setOrganism(newOrganism);
    }

    function handleCancel(){
        history.push('/admin')
    }

    return(<>
    <div className="add-organism-container">
        <h2 className="center-text">Add Organism</h2>
        {errors.length > 0 ? <Error errors={errors}/> : null}
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <label htmlFor="commonName">Common Name</label>
                <input name="commonName" type="text" className="form-control mb-3" id="commonName" value={organism.common_name} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="scientificName">Scientific Name</label>
                <input name="scientificName" type="text" className="form-control mb-3" id="scientificName" value={organism.scientific_name} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <label htmlFor="category">Category</label>
                <input name="category" type="text" className="form-control mb-3" id="category" value={organism.category} onChange={handleChange}/>
            </div>
            <div className="form-group">
                <button type="submit" className="btn btn-success mt-3">Submit</button>
                <button type="button" className="btn btn-danger mt-3" onClick={handleCancel}>Cancel</button>
            </div>
        </form>
        </div>
    </>);
}

export default OrganismAddForm;
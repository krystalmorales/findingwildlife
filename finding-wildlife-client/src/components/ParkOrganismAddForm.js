import { useEffect, useState, useContext } from "react";
import { useHistory } from "react-router-dom";
import ParkSelection from "./ParkSelection";
import OrganismSelection from "./OrganismSelection";
import Error from "./Error";
import UserContext from "../UserContext";

const DEFAULT_PARK_ORGANISM = { abundance: '', nativeness: '', parkId: '', organism: {organismId: '', commonName: '', scientificName: '', category: ''} }

function ParkOrganismAddForm(){

    const [parks, setParks] = useState([]);
    const [organisms, setOrganisms] = useState([]);
    const [parkOrganism, setParkOrganism] = useState([DEFAULT_PARK_ORGANISM]);
    const [errors, setErrors] = useState([])

    const history = useHistory();
    const authManager = useContext(UserContext);

    useEffect(() => {
        fetch('http://localhost:8080/finding-wildlife/park')
        .then(response => {
          if(response.status === 200) {
            return response.json();
          }
          return Promise.reject('Something Went Wrong!');
        })
        .then(data => {
          setParks(data);
        })
        .catch(error => history.push('/error', {errorMessage: error}));
      }, []);

    function handleParkChange(event){


        fetch(`http://localhost:8080/finding-wildlife/park-organism/${event.target.value}`)
        .then(response => {
            if(response.status === 200) {
            return response.json();
            }
            return Promise.reject('Something Went Wrong!');
        })
        .then(data => {
            setOrganisms(data);
        })
        .catch(error => history.push('/error', {errorMessage: error}));
    };

    function addParkOrganism(){
        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${authManager.user.token}`
            },
            body: JSON.stringify({...parkOrganism})
        };

        fetch('http://localhost:8080/finding-wildlife/park-organism', init)
        .then(response => {
            if(response.status === 201 || response.status === 400){
                return response.json();
            }
            return Promise.reject('Something Went Wrong!');
        })
        .then(body => {
            if(body.parkOrganismId){
                history.push('/admin')
            }
            else if(body){
                setErrors(body);
            }
        })
        .catch(error => history.push('/error', {errorMessage: error}));
    }

    function handleChange(event){
        const property = event.target.name;
        const value = event.target.value;

        const newParkOrganism = {...parkOrganism};

        newParkOrganism[property] = value;
        console.log(newParkOrganism)
        setParkOrganism(newParkOrganism);
    }

    function handleOrganismChange(event){
        const orgValue = event.target.value;
        for(let i = 0; i<organisms.length; i++){
            if(orgValue === organisms[i].organismId){
                setParkOrganism(organisms[i])
                break;
            }
        }
    }

    function onSubmit(event){
        event.preventDefault();

        const fetchFunction = addParkOrganism;

        fetchFunction();
    }

    function handleCancel(){
        history.push('/admin')
    }

    return(<>
        <h2>Add Wildlife to Park</h2>
        {errors.length > 0 ? <Error errors={errors}/> : null}
        <form onSubmit={onSubmit}>
            <div className='form-group'>
            <select className="custom-select custom-select-lg mb-3" onChange={handleParkChange}>
                <option defaultValue="" >Select National Park</option>
                    {parks.map(park => <ParkSelection key={park.parkId} park={park}/>)}
                </select>
            </div>
            <div className='form-group'>
            <select className="custom-select custom-select-lg mb-3" onChange={handleOrganismChange}>
                <option defaultValue="" >Select Organism</option>
                {organisms.map(organism => <OrganismSelection key={organism.organismId} organism={organism}/>)}
                </select>
            </div>
            <div className="form-group">
                <label htmlFor="abundance">Abundance</label>
                <input name="abundance" type="text" className="form-control" id="abundance" value={parkOrganism.abundance} onChange={handleChange}/>
            </div>
            <div className="form-group">
          <label htmlFor="nativeness">Material</label>
          <select className="form-control" id="nativeness" name="nativeness" value={parkOrganism.nativeness} onChange={handleChange}>
            <option value="">Please Select One</option>
            <option value="NATIVE">Native</option>
            <option value="NONNATIVE">Non-Native</option>
            <option value="UNKNOWN">Unknown</option>
          </select>
        </div>
            <div className="form-group">
                <button type="submit" className="btn btn-success mr-3">Submit</button>
                <button type="button" className="btn btn-danger" onClick={handleCancel}>Cancel</button>
            </div>

        </form>
    </>);
}

export default ParkOrganismAddForm;
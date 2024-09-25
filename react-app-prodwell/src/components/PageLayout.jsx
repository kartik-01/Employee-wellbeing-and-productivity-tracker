import { NavigationBar } from "./NavigationBar";

export const PageLayout = (props) => {
    return (
        <>
            <NavigationBar />
            <br />
            <h5>
                <center>Welcome to ProdWell Pulse Portal</center>
            </h5>
            <br />
            {props.children}
            <br />
        </>
    );
}
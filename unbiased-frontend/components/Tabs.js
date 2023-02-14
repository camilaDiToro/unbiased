import { useRouter } from "next/router";
import Link from 'next/link';
import {useState} from "react";
import types from "../types";



export default function Tabs(props) {
    const router = useRouter();
    const {items, selected, children, className} = props



   if (props.pill) {
       const activeClasses = "bg-info active"
       const inactiveClasses = "text-secondary"
       return <>
           <ul data-testid="not-pills" className={`${className} mb-4 mt-4 nav bg-transparent nav-pills text-light p-2 rounded-lg d-flex `}>
               {items.map(item => <li className="nav-item" key={item.text}>
                   <Link data-testid="tab-link" className={`text-capitalize nav-link fromLeft rounded-pill hover-pill ml-1 ${selected === item.text ? activeClasses : inactiveClasses}`}
                         aria-current="page"
                         href={{
                             pathname: router.pathname,
                             query: { ...router.query, ...item.params },
                         }}

                   >
                       {item.text}
                   </Link>
               </li>)}
               {children}
           </ul>
       </>
    } else {
       return<>
           <ul data-testid="not-pills" className="my-2 nav nav-tabs justify-content-center text-light p-2">
               {items.map(item => <li className="nav-item" key={item.text}>
                   <Link className={`text-capitalize text-white nav-link tabs ${selected === item.text ? 'active' : ''}`}
                         aria-current="page"
                         href={{
                             pathname: router.pathname,
                             query: { ...router.query, ...item.params },
                         }}

                   >
                       {item.text}
                   </Link>
               </li>)}

           </ul>
       </>
    }
}

Tabs.propTypes = types.Tabs
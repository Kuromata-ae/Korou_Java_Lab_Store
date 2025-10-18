// usePromoCodes.ts
import { useEffect, useState } from 'react';
import { fetchPromoCodes } from '../services/promocodes';
export function usePromoCodes() {
    const [data, setData] = useState<PromoCode[]>([]);
    useEffect(() => {
        fetchPromoCodes().then(res => setData(res.data));
    }, []);
    return data;
}

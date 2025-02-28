-- FUNCTION: public.fn_inventory(text, integer, text, integer)

-- DROP FUNCTION IF EXISTS public.fn_inventory(text, integer, text, integer);

CREATE OR REPLACE FUNCTION public.fn_inventory(
	p_option text,
	p_sku integer DEFAULT NULL::integer,
	p_name text DEFAULT NULL::text,
	p_quantity integer DEFAULT NULL::integer)
    RETURNS TABLE(status text, message text, sku integer, name text, quantity integer) 
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
    ROWS 1000

AS $BODY$
DECLARE
    tabla_existe BOOLEAN;
BEGIN
    -- Verificar si la tabla 'inventory' existe; si no, crearla
    SELECT EXISTS (
        SELECT 1
        FROM information_schema.tables
        WHERE table_name = 'inventory'
        AND table_schema = 'public'
    ) INTO tabla_existe;

    IF NOT tabla_existe THEN
        CREATE TABLE public.inventory (
            sku INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            quantity INTEGER NOT NULL CHECK (quantity >= 0)
        );
    END IF;

    IF p_option = 'Create' THEN
        BEGIN
            INSERT INTO public.inventory (sku, name, quantity) VALUES (p_sku, p_name, p_quantity);
            RETURN QUERY SELECT 'SUCCESS' AS status, 'Producto creado exitosamente.' AS message, s.sku, s.name, s.quantity
            FROM public.inventory s WHERE s.sku = p_sku;
        EXCEPTION
            WHEN unique_violation THEN
                RETURN QUERY SELECT 'FAILURE' AS status, 'El SKU ya existe.' AS message, NULL::integer, NULL::text, NULL::integer;
            WHEN others THEN
                DECLARE
                    v_error_message TEXT;
                BEGIN
                    GET STACKED DIAGNOSTICS v_error_message = MESSAGE_TEXT;
                    RETURN QUERY SELECT 'FAILURE' AS status, 'Error al crear el producto: ' || v_error_message AS message, NULL::integer, NULL::text, NULL::integer;
                END;
        END;

    ELSIF p_option = 'Delete' THEN
        BEGIN
            IF p_sku IS NULL THEN
                RETURN QUERY SELECT 'FAILURE' AS status, 'El SKU es obligatorio para eliminar un producto.' AS message, NULL::integer, NULL::text, NULL::integer;
                RETURN;
            END IF;

            DELETE FROM public.inventory s WHERE s.sku = p_sku;
            IF FOUND THEN
                RETURN QUERY SELECT 'SUCCESS' AS status, 'Producto eliminado exitosamente.' AS message, NULL::integer, NULL::text, NULL::integer;
            ELSE
                RETURN QUERY SELECT 'FAILURE' AS status, 'Producto no encontrado.' AS message, NULL::integer, NULL::text, NULL::integer;
            END IF;
        EXCEPTION
            WHEN others THEN
                DECLARE
                    v_error_message TEXT;
                BEGIN
                    GET STACKED DIAGNOSTICS v_error_message = MESSAGE_TEXT;
                    RETURN QUERY SELECT 'FAILURE' AS status, 'Error al eliminar el producto: ' || v_error_message AS message, NULL::integer, NULL::text, NULL::integer;
                END;
        END;

    ELSIF p_option = 'Select' THEN
        RETURN QUERY SELECT 'SUCCESS' AS status, 'Operación exitosa.' AS message, s.sku, s.name, s.quantity FROM public.inventory s;

    ELSIF p_option = 'SelectById' THEN
        BEGIN
            RETURN QUERY SELECT 'SUCCESS' AS status, 'Operación exitosa.' AS message, s.sku, s.name, s.quantity
            FROM public.inventory s WHERE s.sku = p_sku;
            IF NOT FOUND THEN
                RETURN QUERY SELECT 'FAILURE' AS status, 'Producto no encontrado.' AS message, NULL::integer, NULL::text, NULL::integer;
            END IF;
        EXCEPTION
            WHEN others THEN
                DECLARE
                    v_error_message TEXT;
                BEGIN
                    GET STACKED DIAGNOSTICS v_error_message = MESSAGE_TEXT;
                    RETURN QUERY SELECT 'FAILURE' AS status, 'Error al obtener el producto: ' || v_error_message AS message, NULL::integer, NULL::text, NULL::integer;
                END;
        END;

    ELSIF p_option = 'Update' THEN
        BEGIN
            IF p_sku IS NULL THEN
                RETURN QUERY SELECT 'FAILURE' AS status, 'El SKU es obligatorio para actualizar un producto.' AS message, NULL::integer, NULL::text, NULL::integer;
                RETURN;
            END IF;

            IF p_name IS NULL AND p_quantity IS NULL THEN
                RETURN QUERY SELECT 'FAILURE' AS status, 'Debe proporcionar al menos un campo para actualizar.' AS message, NULL::integer, NULL::text, NULL::integer;
                RETURN;
            END IF;

            EXECUTE format('UPDATE public.inventory s SET %s WHERE s.sku = %s',
                concat_ws(', ',
                    CASE WHEN p_name IS NOT NULL THEN format('name = %L', p_name) ELSE NULL END,
                    CASE WHEN p_quantity IS NOT NULL THEN format('quantity = %s', p_quantity::text) ELSE NULL END
                ),
                p_sku
            );

            IF FOUND THEN
                RETURN QUERY SELECT 'SUCCESS' AS status, 'Producto actualizado exitosamente.' AS message, s.sku, s.name, s.quantity
                FROM public.inventory s WHERE s.sku = p_sku;
            ELSE
                RETURN QUERY SELECT 'FAILURE' AS status, 'Producto no encontrado.' AS message, NULL::integer, NULL::text, NULL::integer;
            END IF;
        EXCEPTION
            WHEN others THEN
                DECLARE
                    v_error_message TEXT;
                BEGIN
                    GET STACKED DIAGNOSTICS v_error_message = MESSAGE_TEXT;
                    RETURN QUERY SELECT 'FAILURE' AS status, 'Error al actualizar el producto: ' || v_error_message AS message, NULL::integer, NULL::text, NULL::integer;
                END;
        END;

    ELSE
        RETURN QUERY SELECT 'FAILURE' AS status, 'Opción no válida.' AS message, NULL::integer, NULL::text, NULL::integer;
    END IF;
END;
$BODY$;

ALTER FUNCTION public.fn_inventory(text, integer, text, integer)
    OWNER TO postgres;
